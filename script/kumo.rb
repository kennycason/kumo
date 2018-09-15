class Kumo < Formula
  desc "Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "https://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.17/kumo-cli-1.17.jar"
  sha256 "17f84d6287eeccf361c9eabe6d7449983c13d777afd161105a324c00adcadd0e"

  depends_on :java => "1.8+"

  def install
    libexec.install "kumo-cli-1.17.jar"
    bin.write_jar_script libexec/"kumo-cli-1.17.jar", "kumo"
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
    pipe_output("#{bin}/kumo -i https://wikipedia.org -o /tmp/wikipedia.png", "Generate simple wordcloud")
  end
end
