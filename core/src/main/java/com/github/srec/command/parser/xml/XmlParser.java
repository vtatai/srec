package com.github.srec.command.parser.xml;

import com.github.srec.command.*;
import com.github.srec.command.method.MethodCallCommand;
import com.github.srec.command.method.MethodCommand;
import com.github.srec.command.method.MethodParameter;
import com.github.srec.command.method.MethodScriptCommand;
import com.github.srec.command.parser.ParseError;
import com.github.srec.command.parser.ParseException;
import com.github.srec.command.parser.ParseLocation;
import com.github.srec.command.parser.Parser;
import com.github.srec.command.value.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parses an XML file.
 *
 * XML parsing is different from DSL parsing because it requires types for method parameters since these types cannot be
 * inferred simply from the contents of the argument passed (is 100 the string "100" or is it the number 100?). One
 * option would be to require single quotes inside the double quotes for string parameters, but that is ugly, verbose
 * and counter-intuitive. 
 *
 * @author Victor Tatai
 */
public class XmlParser implements Parser {
    private File parsingFile;
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
    private BlockCommand currentBlock;
    private List<ParseError> errors = new ArrayList<ParseError>();

    @Override
    public TestSuite parse(ExecutionContext context, File file) {
        try {
            return parse(context, new FileInputStream(file), file);
        } catch (FileNotFoundException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public TestSuite parse(ExecutionContext context, InputStream is, File file) {
        try {
            parsingFile = file;
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
        } else if ("test_case".equals(name)) {
            currentTestCase = new TestCase(getAttributeByName("name", element), new ExecutionContext(contextPrototype));
        } else if ("def".equals(name)) {
            currentBlock = new MethodScriptCommand(getAttributeByName("name", element), parsingFile);
        } else if ("parameter".equals(name)) {
            Type type = parseType(getAttributeByName("type", element));
            if (type == null) throw new ParseException("'type' attribute missing for argument");
            ((MethodScriptCommand) currentBlock).addParameter(new MethodParameter(getAttributeByName("name", element), type));
        } else {
            ExecutionContext executionContext = getCurrentExecutionContext();
            CommandSymbol symbol = executionContext.findSymbol(name);
            if (symbol == null || !(symbol instanceof MethodCommand)) throw new ParseException("Method with name '" + name + "' not found");
            final MethodCallCommand command = new MethodCallCommand(name, createParseLocation(element));
            Iterator it = element.getAttributes();
            while (it.hasNext()) {
                Attribute attr = (Attribute) it.next();
                final String attributeName = attr.getName().getLocalPart();
                MethodParameter methodParameter = ((MethodCommand) symbol).getParameters().get(attributeName);
                if (methodParameter == null) throw new ParseException("Parameter " + attributeName + " not defined");
                command.addParameter(attributeName, createLiteralCommand(attr.getValue(), methodParameter.getType()));
            }
            if (currentBlock != null) {
                currentBlock.addCommand(command);
            } else {
                executionContext.addCommand(command);
            }
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
                return new LiteralCommand(new NilValue());
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
            context.addSymbol((MethodScriptCommand) currentBlock);
            currentBlock = null;
        }
    }

    private ParseLocation createParseLocation(XMLEvent event) {
        return new ParseLocation(getParsingFileCanonicalPath(), event.getLocation().getLineNumber(),
                event.getLocation().getColumnNumber(), event.toString());
    }

    private String getParsingFileCanonicalPath() {
        try {
            return parsingFile != null ? parsingFile.getCanonicalPath() : null;
        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    @Override
    public List<ParseError> getErrors() {
        return errors;
    }
}
