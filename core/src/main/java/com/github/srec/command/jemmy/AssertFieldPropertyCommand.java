package com.github.srec.command.jemmy;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;
import com.github.srec.jemmy.JemmyDSL;
import com.github.srec.jemmy.JemmyDSL.Window;

import org.netbeans.jemmy.JemmyException;
import org.testng.Assert;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Asserts a field property that has a javabean-style getter.
 * The value used to compare can be configured through a test property 
 * or a string.
 */
@SRecCommand
public class AssertFieldPropertyCommand extends JemmyEventCommand {
    
    public enum PropertyType{
        PROPERTY,
        STRING
    }

    public AssertFieldPropertyCommand() {
        super("assert_field_property", params("locator", Type.STRING,
                                              "expectedObjectType", Type.STRING, 
                                              "property", Type.STRING, 
                                              "expectedObject", Type.STRING));
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected void runJemmy(ExecutionContext ctx, 
            Map<String, Value> params) throws JemmyException {
        String locator = coerceToString(params.get(LOCATOR), ctx);
        String expectedProperty = coerceToString(
                params.get("property"), ctx);
        String expectedObject = coerceToString(
                params.get("expectedObject"), ctx);
        PropertyType expectedType = coerceToEnum(
                params.get("expectedObjectType"), 
                ctx,
                PropertyType.class);
        
        Object obj = getObject(expectedProperty, locator);
        
        switch (expectedType) {
            case PROPERTY:
                Object expectedObj = ctx.getTestSuite()
                        .getProperty(expectedObject);
                
                Assert.assertEquals(expectedObj, obj);
                break;
            case STRING:
                Assert.assertEquals(expectedObject, (String) obj);
                break;
        }
    }
 
    private Object getObject(String property, String locator) {
        Window window = JemmyDSL.currentWindow();
        Component object = JemmyDSL.findComponent(locator, 
                                                  window.getComponent().getSource());

        try {
            String propertyName = property.substring(0, 1)
                    .toUpperCase() + property.substring(1);
            String propertySub = "get" + propertyName;
            Class<?> cls = object.getClass();

            Method method = null;

            try {
                method = cls.getMethod(propertySub, (Class[]) null);
            } catch (NoSuchMethodException e) {
                propertySub = "is" + propertyName;
                method = cls.getMethod(propertySub, (Class[]) null);  
            }
             
            return method.invoke(object, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}