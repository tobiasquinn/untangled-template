EXAMPLE_PROJECT=example-project

cd ~/projects/untangled-template
rm -r $EXAMPLE_PROJECT
lein install &&
lein new untangled $EXAMPLE_PROJECT -- :devcards :server &&
cd $EXAMPLE_PROJECT &&
rlwrap lein run -m clojure.main
