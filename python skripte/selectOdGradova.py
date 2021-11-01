ulaz = file("gradoviSortirani", "r")
izlaz = file("gradoviSortiraniSelect","w")
sve = ulaz.read();
niz = sve.split("\n")
for s in niz:
  string="<option value=\""+s+"\">"+s+"</option>\n"
  izlaz.write(string)
izlaz.flush()
  