# TDE 

Vídeo: https://youtu.be/bRaY_mFgK5s

Rafael Schuck Segatto e Rafael Brennsen

## Como rodar

cd parte1-filosofos && javac *.java && java FilosofosIngenuo && java FilosofosHierarquia
cd parte2-semaforo && javac Contador.java && java Contador
cd parte3-deadlock && javac *.java && java DeadlockTravado && java DeadlockCorrigido

## Parte 1 - Filósofos

No ingênuo cada filósofo pega o garfo da esquerda e depois o da direita. Quando todo mundo pega o da esquerda junto, fica cada um esperando o vizinho soltar e trava tudo. Coloquei um sleep entre os dois garfos pra travar mais fácil.

Pra resolver, fiz cada um pegar primeiro o garfo de número menor. Na prática só o filósofo 4 muda (pega o 0 antes do 4), e isso já quebra a espera circular. Usei semáforo justo pra ninguém ficar sem comer.

pega min(esq, dir)
pega max(esq, dir)
come
solta os dois

O ingênuo para com "DEADLOCK: ninguem come ha mais de 3s" e a versão corrigida termina com os 5 filósofos tendo comido 10 vezes cada.

## Parte 2 - Semáforo

8 threads fazendo 200.000 incrementos cada, então era pra dar 1.600.000.

| Versão | Exec | Resultado | Tempo |
|---|---|---|---|
| Sem sincronização | 1 | 258.690 | 65 ms |
| Sem sincronização | 2 | 607.408 | 0 ms |
| Sem sincronização | 3 | 215.786 | 2 ms |
| Com semáforo | 1 | 1.600.000 | 6954 ms |
| Com semáforo | 2 | 1.600.000 | 7094 ms |
| Com semáforo | 3 | 1.600.000 | 7011 ms |

Sem semáforo perde incremento porque `count = count + 1` não é atômico, duas threads leem o mesmo valor e uma soma some. Com o semáforo só passa uma thread por vez (e o release/acquire garante happens-before), aí dá certo, mas fica bem mais lento.

## Parte 3 - Deadlock

T1 pega A e depois B, T2 pega B e depois A. As duas ficam esperando uma pela outra e o programa trava (ele detecta isso e mostra "DEADLOCK detectado" com T1 e T2). Aqui aparecem as 4 condições de Coffman.

Pra corrigir as duas pegam sempre A antes de B, então não tem mais espera circular e o programa termina normal.
