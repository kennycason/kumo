class Kumo < Formula
  desc "Kumo: Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "http://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.10/kumo-cli-1.10.jar"
  sha256 "3a0399c0937053813cd0cefa7437acbeed460fb18ba20ed5b5483ee591f34295"

  def install
    libexec.install "kumo-cli-1.10.jar"
    bin.write_jar_script libexec/"kumo-cli-1.10.jar", "kumo"
    puts "Finished installing kumo 1.10"
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