require 'test_form_def.rb'

window_activate "TestForm"
select "calculationCB", "Future Value"
type "initialValueTF", "100"
type_special "initialValueTF", "Tab"
assert "initialValueTF", "100"
select "calculationCB", "index=0"
click "text=Ok"
close_test_form
