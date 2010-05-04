window_activate "TestForm"

def test
    select "calculationCB", "Future Value"
    type "initialValueTF", "100"
    type_special "initialValueTF", "Tab"
    assert "initialValueTF", "100"
    select "calculationCB", "index=0"
    click "text=Ok"
    close "TestForm"
end

test