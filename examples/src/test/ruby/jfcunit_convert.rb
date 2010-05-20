# This script can be used to convert JFCUnit scripts to srec scripts
# REQUIRES RUBY 1.9
# THIS IS STILL EXPERIMENTAL CODE
# Author: Victor Tatai

require_relative 'custom_tags.rb'

require 'rubygems'
require 'xml'
require 'set'

include XML

class XmlReader
  def initialize(dir_per_suite = true)
    @dir_per_suite = dir_per_suite
  end

  def read(filename)
    XML.default_line_numbers = true
    parser = Reader.file(filename)
    while parser.read do
      node(parser)
    end
  end

  private
  def node(reader)
    name = reader.name
    return if name == "#text"
    # puts "READ #{name}"
    case name
    when "key"
      if reader['string']
        @file.puts "type \"id=#{reader['refid']}\", \"#{reader['string']}\""
      elsif reader['code']
        @file.puts "type_special \"id=#{reader['refid']}\", \"#{translate_type_special(reader['code'])}\""
      end
    when "find"
      if reader['name'] && reader['finder'] == "NamedComponentFinder"
        @file.puts "find \"#{reader['name']}\", \"#{reader['id']}\", \"#{translate_find_class(reader['class'])}\""
      elsif reader['finder'] == "FrameFinder"
        @file.puts "window_activate \"#{reader['title']}\""
      elsif reader['finder'] == "DialogFinder"
        @file.puts "dialog_activate \"#{reader['title']}\""
      end
    when "asserttextfieldcontains"
      @file.puts "assert \"id=#{reader['id']}\", \"#{reader['value']}\""
    when "click"
      if reader['type'] == "JComboBoxMouseEventData" && reader['index']
        @file.puts "select \"id=#{reader['refid']}\", \"index=#{reader['index']}\""
      elsif reader['type'] == "MouseEventData" && reader['refid']
        @file.puts "click \"id=#{reader['refid']}\""
      end
    when "taghandlers"
      raise "No custom tag defined for #{reader['tagname']}" if !($custom_tags.include? reader['tagname'])
    when "suite"
      if @dir_per_suite
        @suite_name = reader['name']
        FileUtils.mkdir_p @suite_name
      end
    when "test"
      if @suite_name
        @file = File.new(@suite_name + '/' + reader['name'] + '.rb', "w+")
      else
        @file = File.new(reader['name'] + '.rb', "w+")
      end
    when "assertenabled"
      @file.puts "assert_enabled \"id=#{reader['refid']}\", #{reader['enabled']}"
    when "assertequals"
      @file.puts "assert_equals \"id=#{reader['actualrefid']}\", \"#{reader['expectedobj']}\""
    when "evaluate"
      @file.puts "evaluate \"id=#{reader['refid']}\", \"#{reader['method']}\", \"#{reader['id']}\""
    when "#comment"
      @file.puts "\# #{reader.value}"
    else
      if $custom_tags.include? name
        tag = $custom_tags[name]
        @file.puts "#{tag[0]} #{join_params(reader, tag)}"
      else
        raise "Line #{reader.line_number}: Unrecognized tag '#{name}'"
      end
    end
  end

  def join_params(reader, tag)
    # puts "Name: #{reader.name}"
    value = ''
    for i in 1..tag.size - 1
      if tag[i].nil? || reader[tag[i]].nil?
        value << "\"\","
      else
        value << "\"" << reader[tag[i]] << "\","
      end
    end
    return value[0, value.length - 1] if value[value.length - 1, value.length - 1] == ","
    value
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
    when "javax.swing.JTable"
      "table"
    else
      raise "Unsupported find class name #{clName}"
    end
  end

  def translate_type_special(text)
    case text
    when "VK_TAB"
      "Tab"
    when "VK_END"
      "End"
    when "VK_BACK_SPACE"
      "Backspace"
    else
      raise "Not supported type_special character #{text}"
    end
  end
end

XmlReader.new.read ARGV[0]
