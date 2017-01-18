#!/usr/bin/env bash

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# ------------------------------------------------------------------
#
# This script collects the *.md files and other resources needed to generate
# a book-like collection of end-user documentation.  
#
# The Metron development community has chosen to do most documentation in README.md
# files, because they are easy to write and maintain, and located near the code they
# document. Also they are versioned along with that code, so they are always in sync
# with the particular version being considered.
#
# However, the location of the various README.md files in github are not necessarily
# obvious to non-developers, and can therefore be difficult to find and use.
# In order to make the files easier to use as end-user documentation, we collect them
# into a book-like collection.  It should perhaps be viewed as a collection of essays,
# since each README.md file is written independently.

## This script assumes it is running at $METRON_SOURCE/site-book/bin/
METRON_SOURCE=`cd $(dirname $0); cd ../..; pwd`

## Maintainers set EXCLUSION_LIST to a list of egrep-style regular expressions.
## Any file path that matches any of these patterns will be excluded.
## Please note that the file paths being matched are output of 'find', rooted at
## $METRON_SOURCE.  'Find' will start each path with './', which is matched by '^\./'.
## Please place each regex in single quotes, and don't forget to backslash-escape
## literal periods and other special characters.
EXCLUSION_LIST=(
    '/site/'
    '/site-book/'
    '/build_utils/'
)

## This is a list of resources (eg .png files) needed to render the markdown files.
## Each entry is a file path, relative to $METRON_SOURCE.
## Note: any images in site-book/src/site/resources/image-archive/ will also be included.
RESOURCE_LIST=(
    metron-platform/metron-parsers/parser_arch.png
    metron-platform/metron-indexing/indexing_arch.png
    metron-platform/metron-enrichment/enrichment_arch.png
)

######################
## Proceed

cd "$METRON_SOURCE"
mkdir -p "$METRON_SOURCE"/site-book/src/site/markdown \
    "$METRON_SOURCE"/site-book/src/site/resources/images

# cons up the exclude exec string
cmd=""
for exclusion in "${EXCLUSION_LIST[@]}" ; do
    cmd="${cmd} | egrep -v '${exclusion}'"
done

# Capture the hierarchical list of .md files.
# Take them all, not just README.md files.
cmd="find . -name '*.md' -print ${cmd}"
echo Collecting markdown files: $cmd
MD_FILE_LIST=( `eval $cmd` )

# Pipe the files into the src/site/markdown directory tree
tar cvf - "${MD_FILE_LIST[@]}" | ( cd "$METRON_SOURCE"/site-book/src/site/markdown; tar xf -  )

# Grab the other resources needed
echo " "
echo Collecting additional resource files:
for r in "${RESOURCE_LIST[@]}" site-book/src/site/resources/image-archive/* ; do
    echo ./"$r"
    cp "$r" "$METRON_SOURCE"/site-book/src/site/resources/images/
done



