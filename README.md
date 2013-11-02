# bencoding-clj

This is a simple, quick, and *dirty* library for Bencoding.

Bencode is a simple serialization format, popularly used by BitTorrent. For more information about the format, see:

http://en.wikipedia.org/wiki/Bencode
http://jonas.nitro.dk/bittorrent/bittorrent-rfc.html#anchor7

I wrote this to assist with communicating to a CJDNS server.

## Usage

In your project.clj dependencies:
	[bencoding-clj "0.1.1-SNAPSHOT"]

In your code:

	(:require [bencoding-clj.core :as benc :only [encode decode]])

Then call the encode and decode functions:

	(benc/decode "d4:argsd4:pagei0ee1:q24:Admin_availableFunctionse")
	(benc/encode {"q" "ping"})

## Horrible Disclaimer

Untested, slow, and buggy.

The decoding part should be totally rewritten using something like Parsley.

## License

Copyright © 2013 Robbie Huffman 

Distributed under the Eclipse Public License, the same as Clojure.
