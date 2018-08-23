class Kumo < Formula
  desc "Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "https://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.14-SNAPSHOT/kumo-cli-1.14-SNAPSHOT.jar"
  sha256 "c9ad525f7d6aec9e2c06cf10017e1e533f43b1b3c4df5aa0d4b137f8d563c5c6"

  depends_on :java => "1.8+"

  def install
    libexec.install "kumo-cli-1.14-SNAPSHOT.jar"
    bin.write_jar_script libexec/"kumo-cli-1.14-SNAPSHOT.jar", "kumo"
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
    pipe_output("#{bin}/kumo -i https://wikipedia.org -o /tmp/wikipedia.png", "Generate simple wordcloud")
  end
end
ku