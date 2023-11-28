## Равновесие Нэша
Программа считает равновесие Нэша для игры в "крестики-нолики" и гарантированно не проигрывает. Класс можно переиспользовать для любой клеточной игры, подменив field, score, possibleMoves (например так https://github.com/Roma321/ConnectFour/blob/refactor/app/src/main/java/com/example/inrow/bot/NashEquilibriumBot.kt). Оптимизация чуть хуже нуля. Глубину просчёта можно задать в константе. Параметр **score** предполагается от -1 до 1.

В каждом элементе дерева хранится оценка позиции для того игрока, чей сейчас ход, а также ход, который был сделан перед тем, как получилась эта позиция. Можно сказать, что элементы хранят ссылки на рёбра дерева, которые соединяют их с родителем. Так как в каждом элементе хранится score для игрока, чей сейчас ход, то для принятия решения выбирается тот дочерний элемент, где оценка позиции наименьшая (худшая для соперника = лучшая для того, кто делает ход)