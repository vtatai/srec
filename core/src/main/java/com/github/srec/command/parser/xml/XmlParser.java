package com.github.srec.command.parser.xml;

import com.github.srec.Location;
import com.github.srec.command.*;
import com.github.srec.command.method.MethodCallCommand;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.method.MethodScriptCommand;
import com.github.srec.command.parser.ParseError;
import com.github.srec.command.parser.ParseException;
import com.github.srec.command.parser.Parser;
import com.github.srec.command.value.*;
import com.github.srec.util.Resource;
import com.github.srec.util.ResourceFactory;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Parses an XML file.<br><br>
 *
 * XML parsing is different from DSL parsing because it requires types to be specified for method parameters since these
 * cannot be inferred simply from the contents of the argument passed (is 100 the string "100" or is it the number 100?).
 * One alternative would be to require single quotes inside the double quotes for string parameters, but that is ugly,
 * verbose and counter-intuitive.
 *
 * @author Victor Tatai
 */
public class XmlParser implements Parser {
    private static final Logger log = Logger.getLogger(XmlParser.class);
    private String parsingFile;
    private Class<?>[] ignoredEvents = new Class<?>[] {Attribute.class, Characters.class, Comment.class, DTD.class,
            EndDocument.class, EntityDeclaration.class, EntityReference.class, Namespace.class, NotationDeclaration.class,
            ProcessingInstruction.class, StartDocument.class};
    private TestSuite currentTestSuite;
    private TestCase currentTestCase;
    /**
     * This is the context prototype which is used to instantiate new ECs for each test case. Methods declared in suite
     * scope are added here, methods in test_case scope are added to the test case EC.
     */
    private ExecutionContext contextPrototype;
    private final Stack<BlockCommand> currentBlocks = new Stack<BlockCommand>();
    private List<ParseError> errors = new ArrayList<ParseError>();
    private boolean parseSymbolsOnly;

    @Override
    public TestSuite parse(ExecutionContext context, File file) {
        try {
            return parse(context, new FileInputStream(file), file.getCanonicalPath());
        } catch (FileNotFoundException e) {
            throw new ParseException(e);
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public TestSuite parse(ExecutionContext context, InputStream is, String fileName) {
        try {
            parsingFile = fileName;
            contextPrototype = context;
            
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLEventReader reader = factory.createXMLEventReader(is);
            while (reader.hasNext()) {
                processEvent(reader.nextEvent());
            }
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        if (currentTestSuite == null) throw new ParseException("No suite found");
        return currentTestSuite;
    }

    private void processEvent(XMLEvent event) {
        if (event == null || isIgnored(event)) return;
        if (event instanceof StartElement) {
            processStartElement((StartElement) event);
        } else if (event instanceof EndElement) {
            processEndElement((EndElement) event);
        }
    }

    private boolean isIgnored(XMLEvent event) {
        for (Class<?> ignoredEvent : ignoredEvents) {
            if (ignoredEvent.isAssignableFrom(event.getClass())) return true;
        }
        return false;
    }

    private void processStartElement(StartElement element) {
        final String name = element.getName().getLocalPart();
        if ("suite".equals(name)) {
            currentTestSuite = new TestSuite(getAttributeByName("name", element));
            getCurrentExecutionContext().setTestSuite(currentTestSuite);
        } else if ("test_case".equals(name)) {
            currentTestCase = new TestCase(getAttributeByName("name", element), new ExecutionContext(contextPrototype));
            getCurrentExecutionContext().setTestCase(currentTestCase);
        } else if ("require".equals(name)) {
            final String resourceName = getAttributeByName("name", element);
            Resource r = ResourceFactory.getGenericResource(resourceName);
            if (r == null || r.getUrl() == null) {
                error(element, "required file '" + resourceName + "' not found");
                return;
            }
            try {
                XmlParser defsParser = new XmlParser();
                defsParser.setParseSymbolsOnly(true);
                defsParser.parse(getCurrentExecutionContext(), r.getUrl().openStream(), resourceName);
            } catch (IOException e) {
                throw new ParseException("Cannot open required file " + resourceName);
            }
        } else if ("def".equals(name)) {
            pushCurrentBlock(new MethodScriptCommand(getAttributeByName("name", element), parsingFile));
        } else if ("parameter".equals(name)) {
            Type type = parseType(getAttributeByName("type", element));
            if (type == null) throw new ParseException("'type' attribute missing for argument");
            ((MethodScriptCommand) peekCurrentBlock()).addParameter(new MethodParameter(getAttributeByName("name", element), type));
        } else if ("set".equals(name)) {
            if (parseSymbolsOnly && currentBlocks.isEmpty()) return; // if only parsing for symbols AND not inside a method
            final SetCommand command = new SetCommand(createParseLocation(element),
                    new ExpressionCommand(getAttributeByName("expression", element),
                            createParseLocation(element.getAttributeByName(new QName("expression")))),
                    getAttributeByName("var", element));
            addCommand(command);
        } else if ("inc".equals(name)) {
            if (parseSymbolsOnly && currentBlocks.isEmpty()) return; // if only parsing for symbols AND not inside a method
            addCommand(new IncCommand(createParseLocation(element),
                    getAttributeByName("var", element)));
        } else if ("if".equals(name)) {
            pushCurrentBlock(new IfCommand(createParseLocation(element),
                    new ExpressionCommand(getAttributeByName("expression", element),
                            createParseLocation(element.getAttributeByName(new QName("expression"))))));
        } else if ("while".equals(name)) {
            pushCurrentBlock(new WhileCommand(createParseLocation(element),
                    new ExpressionCommand(getAttributeByName("expression", element),
                            createParseLocation(element.getAttributeByName(new QName("expression"))))));
        } else if ("break".equals(name)) {
            addCommand(new BreakCommand(createParseLocation(element)));
        } else {
            if (parseSymbolsOnly && currentBlocks.isEmpty()) return; // if only parsing for symbols AND not inside a method
            ExecutionContext executionContext = getCurrentExecutionContext();
            CommandSymbol symbol = executionContext.findSymbol(name);
            if (symbol == null || !(symbol instanceof MethodCommand)) {
                error(element, "Method with name '" + name + "' not found");
                return;
            }
            final MethodCallCommand command = new MethodCallCommand(name, createParseLocation(element));
            Iterator it = element.getAttributes();
            while (it.hasNext()) {
                Attribute attr = (Attribute) it.next();
                final String attributeName = attr.getName().getLocalPart();
                MethodParameter methodParameter = ((MethodCommand) symbol).getParameters().get(attributeName);
                if (methodParameter == null) throw new ParseException("Parameter " + attributeName + " not defined");
                command.addParameter(attributeName, createLiteralCommand(attr.getValue(), methodParameter.getType()));
            }
            addCommand(command);
        }
    }

    private ExecutionContext getCurrentExecutionContext() {
        return currentTestCase == null ? contextPrototype : currentTestCase.getExecutionContext();
    }

    private Type parseType(String name) {
        if ("string".equals(name)) return Type.STRING;
        if ("boolean".equals(name)) return Type.BOOLEAN;
        if ("date".equals(name)) return Type.DATE;
        if ("number".equals(name)) return Type.NUMBER;
        throw new ParseException("Invalid type " + name);
    }

    private static LiteralCommand createLiteralCommand(String valueString, Type type) {
        switch (type) {
            case STRING:
                return new LiteralCommand(valueString);
            case BOOLEAN:
                return new LiteralCommand(new BooleanValue(valueString));
            case NUMBER:
                return new LiteralCommand(new NumberValue(valueString));
            case DATE:
                try {
                    return new LiteralCommand(new DateValue(valueString));
                } catch (java.text.ParseException e) {
                    throw new ParseException(e);
                }
            case NIL:
                return new LiteralCommand(NilValue.getInstance());
            default:
                throw new ParseException("Type not found " + type);
        }
    }

    private String getAttributeByName(String name, StartElement element) {
        final Attribute attribute = element.getAttributeByName(new QName(name));
        if (attribute == null) return null;
        return attribute.getValue();
    }

    private void processEndElement(EndElement element) {
        final String name = element.getName().getLocalPart();
        if ("test_case".equals(name)) {
            currentTestSuite.addTestCase(currentTestCase);
        } else if ("def".equals(name)) {
            ExecutionContext context = getCurrentExecutionContext();
            context.addSymbol((MethodScriptCommand) popCurrentBlock());
        } else if ("if".equals(name)) {
            addCommand(currentBlocks.pop());
        } else if ("while".equals(name)) {
            addCommand(currentBlocks.pop());
        }
    }

    private Location createParseLocation(XMLEvent event) {
        return new Location(getParsingFileCanonicalPath(), event.getLocation().getLineNumber(),
                event.getLocation().getColumnNumber(), event.toString());
    }

    private String getParsingFileCanonicalPath() {
        return parsingFile != null ? parsingFile : null;
    }

    private void error(XMLEvent event, String message) {
        errors.add(new ParseError(ParseError.Severity.ERROR,
                new Location(parsingFile, event.getLocation().getLineNumber(),
                        event.getLocation().getColumnNumber(), event.toString()),
                message));
        log.warn("Parse error: " + message);
    }

    private void addCommand(Command command) {
        if (currentBlocks.isEmpty()) {
            ExecutionContext context = getCurrentExecutionContext();
            context.addCommand(command);
        } else {
            currentBlocks.peek().addCommand(command);
        }
    }

    private void pushCurrentBlock(BlockCommand block) {
        currentBlocks.push(block);
    }

    private BlockCommand peekCurrentBlock() {
        return currentBlocks.peek();
    }

    private BlockCommand popCurrentBlock() {
        return currentBlocks.pop();
    }

    @Override
    public List<ParseError> getErrors() {
        return errors;
    }

    public boolean isParseSymbolsOnly() {
        return parseSymbolsOnly;
    }

    public void setParseSymbolsOnly(boolean parseSymbolsOnly) {
        this.parseSymbolsOnly = parseSymbolsOnly;
    }
}
