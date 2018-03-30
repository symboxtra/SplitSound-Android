#!/bin/bash

#########################################################
#                                                       #
#            Upload code coverage reports               #
#                                                       #
#########################################################

source ./tokens.sh
bash <(curl -s https://codecov.io/bash) -t $CODE_COV_TOKEN
