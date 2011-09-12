package com.github.srec.command.jemmy;

import static com.github.srec.jemmy.JemmyDSL.table;

import java.util.Map;

import org.netbeans.jemmy.JemmyException;

import com.github.srec.command.ExecutionContext;
import com.github.srec.command.SRecCommand;
import com.github.srec.command.value.Type;
import com.github.srec.command.value.Value;

/**
 * Command related to <assert_cell_not_empty> tag that verifies that 
 * a table's cell is not empty. 
 * 
 * @author Wilian C. B.
 *
 */
@SRecCommand
public class AssertCellNotEmptyCommand extends JemmyEventCommand {

	/**
	 * Initializes command attributes. <br/>
	 */
	public AssertCellNotEmptyCommand() {
        super("assert_cell_not_empty", 
        		params("table", Type.STRING, 
        			   "row", Type.NUMBER,
        			   "column", Type.NUMBER));
    }

	/**
	 * Executes the command that asserts that a table's cell is not empty,
	 * nor null. <br/>
	 */
    @SuppressWarnings("rawtypes")
	@Override
    protected void runJemmy(ExecutionContext ctx, Map<String, Value> params) throws JemmyException {
        table(coerceToString(params.get("table"), ctx))
                .row(coerceToBigDecimal(params.get("row")).intValue())
                .assertNotEmptyColumn(coerceToBigDecimal(params.get("column")).intValue());
    }
}