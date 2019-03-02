(ns com.evanjbowling.base.format)

(def O-9 "0123456789")
(def a-z "abcdefghijklmnopqrstuvwxyz")
(def A-Z "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(def base2 "01")
(def base8 "01234567")
(def base16 (str O-9 "ABCDEF"))
(def base32 (str A-Z "234567"))
(def base32hex (subs (str O-9 A-Z) 0 32))
(def base36 (str O-9 A-Z))
(def base58 "No 0,I,O,l" "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz")
(def base64 (str A-Z a-z O-9 "+/"))
(def base64url (str A-Z a-z O-9 "-_"))

