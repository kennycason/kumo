# Releasing


## Release to Nexus

Kumo uses the `maven release` and `nexus-staging-maven-plugin` plugins to deploy to nexus.

The commands require GPG credentials to sign the jars.
Currently only the owner @kennycason can perform this.

The deploy command:

`mvn release:clean release:prepare release:perform`

This removes the `-SNAPSHOT` from the version, performs and tags a git release, uploads to nexus, then bumps the version to the next SNAPSHOT version.

## Update Brew Install Scripts + Doc

Brew install script must contain the updated version and md5 checksums of the file.

Brew formula page can be found [here](https://formulae.brew.sh/formula/kumo)

Brew formula in GitHub can be found [here](https://github.com/Homebrew/homebrew-core/blob/master/Formula/kumo.rb)

Additionally the README must be updated.