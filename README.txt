﻿=========================================================================
						УНИЧТОЖИТЕЛЬ ТАБЛИЦ
=========================================================================
Разработчик Ежов Д.А.

Приложение, направлено на переименование некорректно названных таблиц с последующим удалением.
Работа приложения делаится на два типа:
1. Переименование
2. Удаление


Для работы с GUI запускается без параметров
Для работы через консоль с параметрами -r(rename) иили -d(drop).

В конфиг файле можно определить тестовый прогон для понимания какие таблицы будут затронуты. 
Параметр: is.execute=true - бой/false - тест

select * from OTZ.dbo.V_E_tablesShredderLog order by dtAction desc