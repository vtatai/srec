#!/usr/bin/ruby

# This script can be used to convert JFCUnit scripts to srec scripts
# THIS IS STILL EXPERIMENTAL CODE
# Author: Victor Tatai

require 'rexml/document'
require 'rexml/streamlistener'
include REXML

class XmlListener
  def xmldecl(version, encoding, standalone)
  end

  def tag_start(name, attributes)
    case name
    when "key" 
      if attributes['string']
        puts "type \"id=#{attributes['refid']}\", \"#{attributes['string']}\""
      elsif attributes['code']
        puts "type_special \"id=#{attributes['refid']}\", \"#{translate_type_special(attributes['code'])}\""
      end
    when "find"
      if attributes['name'] && attributes['finder'] == "NamedComponentFinder"
        puts "find \"#{attributes['name']}\", \"#{attributes['id']}\", \"#{translate_find_class(attributes['class'])}\""
      elsif attributes['finder'] == "FrameFinder"
        puts "window_activate \"#{attributes['title']}\""
      elsif attributes['finder'] == "DialogFinder"
        puts "dialog_activate \"#{attributes['title']}\""
      end
    when "asserttextfieldcontains"
      puts "assert \"id=#{attributes['id']}\", \"#{attributes['value']}\""
    when "click"
      if attributes['type'] == "JComboBoxMouseEventData" && attributes['index']
        puts "select \"id=#{attributes['refid']}\", \"index=#{attributes['index']}\""
      elsif attributes['type'] == "MouseEventData" && attributes['refid']
        puts "click \"id=#{attributes['refid']}\""
      end
    end
  end

  def translate_find_class(clName)
    case clName
    when "javax.swing.JTextField"
      "text_field"
    when "javax.swing.JComboBox"
      "combo_box"
    when "javax.swing.JCheckBox"
      "check_box"
    when "javax.swing.JRadioButton"
      "radio_button"
    when "javax.swing.JButton"
      "button"
    else
      raise "Unsupported find class name #{clName}"
    end
  end

  def translate_type_special(text)
    case text
    when "VK_TAB"
      "Tab"
    else
      raise "Not supported type_special character #{text}"
    end
  end

  def text(text)
  end

  def tag_end(name)
  end
end

source = File.new "../resources/demo1.xml"
Document.parse_stream source, XmlListener.new
