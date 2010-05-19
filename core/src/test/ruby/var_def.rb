test_form = frame "TestForm"
test_form.activate
calculation_cb = combo_box "calculationCB"
calculation_cb.select "Future Value"
initial_value_tf = text_field "initialValueTF"
initial_value_tf.type "100"
initial_value_tf.type_special "Tab"
initial_value_tf.assert_text "100"
calculation_cb.select "index=0"
button("text=Ok").click
check_box("cb1").click
text_field("textField2").assert_enabled
test_form.close
