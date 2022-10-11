#! /usr/bin/env bash
#
# kafka-sound-demo - Audio-based demonstration of Kafka, Kafka Connect, and Kafka Streams
# Copyright © 2023 Chris Egerton (fearthecellos@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#


SCRIPT_DIR=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &> /dev/null && pwd)
REPO_DIR="$SCRIPT_DIR"/..

CONNECTORS_VERSION=1.0.2

maybe_install_connectors() {
  jar=kafka-connect-sound-"$CONNECTORS_VERSION".jar
  if [[ -f "$REPO_DIR"/connect-plugins/"$jar" ]]; then
    echo "Connectors already installed"
  else
    echo "Installing connectors"
    curl -sS -O --output-dir connect-plugins \
      https://repo.maven.apache.org/maven2/io/github/c0urante/kafka-connect-sound/"$CONNECTORS_VERSION"/"$jar"
  fi
  echo
}

ensure_binary_producer() {
  if ! type kafka-binary-producer > /dev/null 2> /dev/null; then
    echo 1>&2 'kafka-binary-producer not found. Please install from ' \
      'https://github.com/C0urante/kafka-tools and add to your $PATH ' \
      'before trying again'
    exit 1
  fi
}

ensure_kcctl() {
  if ! type kcctl > /dev/null 2> /dev/null; then
    echo 1>&2 'kcctl not found. Please install from ' \
      'https://github.com/kcctl/kcctl and add to your $PATH ' \
      'before trying again'
    exit 1
  fi
}

relocate() {
  pushd "$REPO_DIR" > /dev/null
  trap 'popd > /dev/null' EXIT
}

cmd() {
  read -p "$@"
  eval "$@"
  echo
}
