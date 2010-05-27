require "java"

java_import com.github.srec.util.Utils

After do |scenario|
  Utils.closeWindows([].to_java(java.awt.Window))
end
