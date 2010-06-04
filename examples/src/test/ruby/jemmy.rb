require "java"
require "test/unit"

include_class "org.netbeans.jemmy.operators.JFrameOperator"
include_class "org.netbeans.jemmy.operators.JInternalFrameOperator"
include_class "org.netbeans.jemmy.operators.JTextFieldOperator"
include_class "org.netbeans.jemmy.operators.JButtonOperator"
include_class "org.netbeans.jemmy.operators.JComboBoxOperator"
include_class "org.netbeans.jemmy.operators.JRadioButtonOperator"
include_class "org.netbeans.jemmy.operators.JCheckBoxOperator"
include_class "org.netbeans.jemmy.operators.JDialogOperator"
include_class "org.netbeans.jemmy.util.NameComponentChooser"
include_class "com.github.srec.util.Utils"

include Test::Unit::Assertions

module Jemmy

    def start(file)
        Utils.runSwingMain "com.github.srec.ui.#{file}", [].to_java(:string)
    end

    def frame(name)
        $current_frame = JFrameOperator.new name
    end

    def iframe(name)
        raise "Current frame cannot be null" if $current_frame.nil?
        $current_iframe = JInternalFrameOperator.new $current_frame, name
    end

    def text_field(name)
        raise "Current frame cannot be null" if $current_frame.nil?
        TextField.new name
    end

    def button(text)
        raise "Current frame cannot be null" if $current_frame.nil?
        Button.new text
    end

    def radio_button(name)
        raise "Current frame cannot be null" if $current_frame.nil?
        RadioButton.new name
    end

    def check_box(name)
        raise "Current frame cannot be null" if $current_frame.nil?
        CheckBox.new name
    end

    def combo_box(name)
        raise "Current frame cannot be null" if $current_frame.nil?
        ComboBox.new name
    end

    def dialog(title)
        Dialog.new title
    end

    class TextField
        def initialize(name)
            frame = $current_iframe.nil? ? $current_frame : $current_iframe 
            @component = JTextFieldOperator.new(frame, NameComponentChooser.new(name))
        end

        def type(str)
            @component.typeText str
        end

        def transfer_focus
            @component.transferFocus
        end

        def assert_text(text)
            assert_equal text, @component.text
        end
    end

    class Button
        def initialize(text)
            frame = $current_iframe.nil? ? $current_frame : $current_iframe
            @component = JButtonOperator.new(frame, text)
        end

        def click
            @component.pushNoBlock
        end
    end

    class ComboBox
        def initialize(name)
            frame = $current_iframe.nil? ? $current_frame : $current_iframe
            @component = JComboBoxOperator.new(frame, NameComponentChooser.new(name))
        end

        def select_item(value)
            @component.selectItem(value)
        end
    end

    class RadioButton
        def initialize(name)
            frame = $current_iframe.nil? ? $current_frame : $current_iframe
            @component = JRadioButtonOperator.new(frame, NameComponentChooser.new(name))
        end

        def click
            @component.pushNoBlock
        end
    end

    class CheckBox
        def initialize(name)
            frame = $current_iframe.nil? ? $current_frame : $current_iframe
            @component = JCheckBoxOperator.new(frame, NameComponentChooser.new(name))
        end

        def click
            @component.pushNoBlock
        end
    end

    class Dialog
        def initialize(title)
            JDialogOperator.waitJDialog title, true, true
            @component = JDialogOperator.new(title)
        end

        def assert_visible
            assert @component.visible 
        end

        def close
            @component.requestClose
        end
    end

    module_function :frame, :iframe, :text_field
end