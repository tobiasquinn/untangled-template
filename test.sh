cd ~/projects/untangled-template
rm -r my-untangled-project
lein install &&
lein new untangled my-untangled-project -- :devcards :server &&
cd my-untangled-project &&
rlwrap lein run -m clojure.main
