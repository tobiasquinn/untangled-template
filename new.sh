EXAMPLE_PROJECT=example-project
EXAMPLE_PROJECT_PATH=example_project

echo "{:port 3000}" > /usr/local/etc/${EXAMPLE_PROJECT_PATH}.edn

cd ~/projects/untangled/template

rm -r $EXAMPLE_PROJECT
lein install &&
    lein new untangled $EXAMPLE_PROJECT -- :devcards :server &&
    cd $EXAMPLE_PROJECT &&
    lein repl
