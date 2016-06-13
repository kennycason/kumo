class Kumo < Formula
  desc "Kumo: Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kumo/1.7/kumo-1.7.jar"
  sha256 "83379e63cbd515c320acdffdc241e709378f1e8977b1feaef04059931110e7c2"

  def install
    libexec.install "kumo-1.7.jar"
    bin.write_jar_script libexec/"kumo-1.7.jar", "kumo"
    puts "Finished installing kumo 1.7"
  end

  def server_script(server_jar); <<-EOS.undent
    #!/bin/bash
    exec java -cp #{server_jar} com.kennycason.kumo.cli.KumoCli "$@"
  EOS
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
  end
end