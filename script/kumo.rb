class Kumo < Formula
  desc "Word Clouds in Java"
  homepage "https://github.com/kennycason/kumo"
  url "https://search.maven.org/remotecontent?filepath=com/kennycason/kumo-cli/1.27/kumo-cli-1.27.jar"
  sha256 "f3643a734e6f8d9c31b0a50f7d4df3f99ea0bf81377efd5a79291b2fff51f1e6"

  depends_on :java => "1.8+"

  def install
    libexec.install "kumo-cli-1.27.jar"
    bin.write_jar_script libexec/"kumo-cli-1.27.jar", "kumo"
  end

  test do
    pipe_output("#{bin}/kumo --version", "Test Kumo version command")
    pipe_output("#{bin}/kumo -i https://wikipedia.org -o /tmp/wikipedia.png", "Generate simple wordcloud")
  end
end
