frame "TestForm" do |frame|
  frame.activate
  calculation_cb = combo_box "calculationCB" do |cb|
    cb.select "Future Value"
  end
  text_field "initialValueTF" do |tf|
    tf.type "100"
    tf.type_special "Tab"
    tf.assert_text "100"
  end
  calculation_cb.select "index=0"
  button("text=Ok").click
  check_box("cb1").click
  text_field("textField2").assert_enabled
  frame.close
end
