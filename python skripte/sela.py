ulaz = file("sela", "r")
izlaz = file("selaIzlaz.txt", "w")
x = ulaz.read();

niz = x.split("\n")
for el in niz:
  
  i=0
  while(el[i]!='\"'):
    i+=1
  novoSelo ="\""
  i+=1
  while(el[i]!='\"'):
    novoSelo+=el[i]
    i+=1
  novoSelo+='\",'
  izlaz.write(novoSelo)
  izlaz.flush()
