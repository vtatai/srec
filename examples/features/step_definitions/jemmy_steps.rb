require 'src/test/ruby/jemmy'

include Jemmy

Given /^a new (\w+) with window "([^\"]+)"/ do |file,frame_name|
    start file
    frame frame_name
end

When /^I fill in (\w+) with "([^\"]+)"/ do |field, value|
    tf = text_field field
    tf.type value
end

When /^I lose focus from (\w+)/ do |field|
    tf = text_field field
    tf.transfer_focus
end

When /^I select combo box (\w+) index (\d+)/ do |combo, index|
    cb = combo_box combo
    cb.select_item index.to_i
end

When /^I select combo box (\w+) value "([^\"]+)"/ do |combo, value|
    cb = combo_box combo
    cb.select_item value
end

Then /(\w+) should contain "([^\"]+)"/ do |field,value|
    tf = text_field field
    tf.assert_text value
end

Then /I should click button "([^\"]+)"/ do |text|
    button(text).click
end

When /I select radio button (\w+)/ do |name|
    radio_button(name).click
end

When /I select check box (\w+)/ do |name|
    check_box(name).click
end

Then /I should see dialog "([^\"]+)"/ do |title|
    dialog(title).assert_visible
end

Then /I close dialog "([^\"]+)"/ do |title|
    dialog(title).close
end
