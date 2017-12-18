class Kumo < Formula
  desc "Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "https://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.12/kumo-cli-1.12.jar"
  sha256 "9d67d943676be57fe75d47f1832c4e8cb415ee8e4e82a71d4d7d1e7b4effe7c4"

  depends_on :java => "1.8+"

  def install
    libexec.install "kumo-cli-1.12.jar"
    bin.write_jar_script libexec/"kumo-cli-1.12.jar", "kumo"
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
    pipe_output("#{bin}/kumo -i https://wikipedia.org -o /tmp/wikipedia.png", "Generate simple wordcloud")
  end
end