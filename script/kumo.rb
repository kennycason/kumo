class Kumo < Formula
  desc "Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "https://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.13/kumo-cli-1.13.jar"
  sha256 "ee811e97e9c23ad91be9e2c15cfbd5717d6dc3281c234d3b20767275142965e6"

  depends_on :java => "1.8+"

  def install
    libexec.install "kumo-cli-1.13.jar"
    bin.write_jar_script libexec/"kumo-cli-1.13.jar", "kumo"
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
    pipe_output("#{bin}/kumo -i https://wikipedia.org -o /tmp/wikipedia.png", "Generate simple wordcloud")
  end
end