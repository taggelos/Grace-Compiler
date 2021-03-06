Με βάση τις παρατηρήσεις που μας είχαν δοθεί αλλά και κάποιες άλλες περιπτώσεις που εντοπίσαμε εκτελώντας τα διάφορα testcases που μας δόθηκαν διορθώσαμε όλες τις περιπτώσεις που μας είχαν ξεφύγει στους ελέγχους στα πρώτα 2 parts.

Για τον ενδιάμεσο κώδικα που παράξαμε έχουμε υλοποιήσει όλες τις περιπτώσεις κι επίσης τον έχουμε ελέγξει με διάφορα testcases που μας έχουν δοθεί αλλά και με δικά μας.

Για τον τελικό κώδικα σε Assembly, τα κομμάτια τα οποία δεν προλάβαμε να υλοποιήσει με επιτυχία (αν και τα φτάσαμε σε ένα σημείο) είναι οι πίνακες (όπου κάτι δεν κάνουμε καλά) και τα strings (char[]) όπου το μόνο που κάναμε είναι να τα γράφουμε στο .data και να τα διαβάζουμε από εκεί.

Για τις συναρτήσεις της standard library της Grace, έχουμε υλοποιήσει τον κώδικα σε assembly για τις: puti, puts, gets, putc και strlen.

Στο bonus μέρος της εργασίας υλοποιήθηκαν τα εξής:
- Constant και Copy Propagation. Υλοποιήθηκε, όμως αντιμέτωπιζε προβλήματα που δεν προλάβαμε να ολοκληρώσουμε, δηλαδή σε περιπτώσεις jump που παράγονταν για παράδειγμα απο while εντολές. Για αυτό τον λόγο, καλείται η συνάρτηση μας με αντίστοιχο flag ώστε να τρέξει σωστά μια πιο απλοποιήμενη μορφή της κατα την οποία διερευνούμε μία εντολή ανάθεσης μόνο αν δεν ξαναχρησιμοποιείται ξανά στο πρόγραμμα μας (checkSameAss).
- Constant folding (constantFolding)
- Αlgebraic Simplification σε περιπτώσεις πρόσθεσης μεταβλητής με 0, αφαίρεσης με 0, πολλαπλασιασμού με 1, διαίρεσης και mod με 1 αλλά και πολλαπλασιασμού με 0. (algebraicSimplification) 

Τα παραπάνω εκτελούνται εξοντωτικά μέχρι να μην υπάρχουν παραπάνω βελτιστοποιήσεις.
Επίσης πραγματοποιείται και 
- Αντιστροφή condition και αποφυγής μιας επιπλέον jump εντολής που παραγόταν. (revertConds)

Έχει υλοποιηθεί και συνάρτηση η οποία ξεχωρίζει τα Βasic Blocks (findBlocks)

Με την εκτέλεση του προγράμματος δημιουργούνται συνολικά 4 αρχεία σε 4 διαφορετικούς φακέλους.
Συγκελριμένα τα αρχεία είναι:
- <όνομα_αρχείου>_ir.txt	στον φάκελο /compilers/ir που περιέχει τον εδιάμεσο κώδικα.
- <όνομα_αρχείου>_irOpt.txt	στον φάκελο /compilers/iropt που περιέχει τον εδιάμεσο κώδικα βελτιστοποιημένο.
- <όνομα_αρχείου>.s 		στον φάκελο /compilers/assemblycode που περιέχει τον κώδικα assembly που παράγεται από τον εδιάμεσο.
- <όνομα_αρχείου>_Opt.s 	στον φάκελο /compilers/assemblycodeopt που περιέχει τον κώδικα assembly που παράγεται από τον βελτιστοποιημένο εδιάμεσο.
