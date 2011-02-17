include_class "com.github.srec.jemmy.JemmyDSL"
include_class "com.github.srec.util.Utils"
include_class "java.awt.event.KeyEvent"

#  Module which contains JRuby wrappers to the JemmyDSL   
module SRec
  # SHORTCUT METHODS

  # Starts an application
  def start(file)
    Utils.runSwingMain "com.github.srec.ui.#{file}", [].to_java(:string)
  end


  def window_activate(window)
    JemmyDSL.frame(window).activate
  end

  def select(locator, item)
    JemmyDSL.combo_box(locator).select(item)
  end

  def type(locator, text)
    JemmyDSL.text_field(locator).type(text)
  end

  def type_special(locator, text)
    JemmyDSL.text_field(locator).type_special(text)
  end

  def assert(locator, text)
    JemmyDSL.text_field(locator).assert_text(text);
  end

  def click(locator)
    JemmyDSL.click(locator)    
  end

  def assert_enabled(locator, enabled = true)
    JemmyDSL.wait_enabled(locator, enabled)
  end

  def assert_checked(locator, enabled = true)
    JemmyDSL.wait_checked(locator, enabled)
  end

  def close(title)
    JemmyDSL.frame(title).close
  end

  # REAL DSL METHODS

  # Finds a frame
  def frame(title, &block)
    frame = JemmyDSL.frame title
    block.call(frame) if block
    frame
  end

  # Finds a text field
  def text_field(locator, &block)
    tf = JemmyDSL.text_field locator
    block.call(tf) if block
    tf
  end

  # Finds a combo box
  def combo_box(locator, &block)
    cb = JemmyDSL.combo_box locator
    block.call(cb) if block
    cb
  end

  # Finds a check box
  def check_box(locator, &block)
    cb = JemmyDSL.check_box locator
    block.call(cb) if block
    cb
  end

  # Finds a button
  def button(locator, &block)
    button = JemmyDSL.button locator
    block.call(button) if block
    button
  end
end
