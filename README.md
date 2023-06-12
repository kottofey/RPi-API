# REST API Endpoints

## Описание
Адрес API для запросов:
```
http://hostname/RPi-API/
```



Объект класса Person имеет следующие поля и типы:
```json
[{
  "id" : int,
  "name" : String,
  "surname" : String,
  "email" : String,
  "phone" : String,
  "country" : String,
  "city" : String,
  "money" : int,
  "itemsInCart" : HashSet<Integer>,
  "itemsOwns" : HashSet<Integer>
}]
```
Поля `itemsInCart` и `itemsOwns` по умолчанию содержат одно значение равное нулю,
что означает пустую корзину или отсутствие предметов во владении. В ином случае,
содержит набор из id предметов. Обновлять и добавлять значения в эти наборы
следует строкой, состоящей из id предметов, разделенных запятой. Все пробелы
из строки будут удалены перед обработкой запроса.

Примеры правильной строки:
- `"1, 2, 3, 4, 99, 1000"`
- `"1,999,2,3,4, 5"`

Все ключи и _строковые_ поля для передачи в JSON строке следует брать в кавычки.
Поле `id` не указывается и добавляется в БД автоматически. Поле `money` является
целочисленным и в кавычки не берется.

Если один из ключей JSON будет указан неправильно, то в таблицу на его место
будет занесёно значение `null` и выводиться не будет. 

Пример правильной строки JSON:

- `{"name": "Roman", "surname": "Lavrov", "email": "email@email.com", "phone": "12345678", "country": "Country", "city": "City", "money": 10902}`

## Описание эндпоинтов:
`/endpoint`
- `метод HTTP запроса`
  - `параметры`
  - `описание`


## `/users`

- **GET**
  - без параметров
  - Выводит список всех пользователей из базы данных в формате JSON

Пример URL:

`http://hostname/RPi-API/users`

Пример вывода:
```json
[
  {
  "id":1,
  "name":"Tasha",
  "surname":"Bean",
  "email":"quisque.porttitor@outlook.com",
  "phone":"7(904)784-13-11",
  "country":"Russian Federation",
  "city":"Orenburg",
  "money":1000,
  "itemsInCart":[1,2,3,4,5],
  "itemsOwns":[666,444,333]
  },
  {
  "id":2,
  "name":"Anne",
  "surname":"Branch", 
  "email":"enim.non.nisi@outlook.com",
  "phone":"7(978)890-72-79",
  "country":"Russian Federation",
  "city":"Volgograd",
  "money":1456,
  "itemsInCart":[0],
  "itemsOwns":[33,11,44]
  },
  {
  ...
  }
]
```

## `/user`

- **GET**
  - Параметры URL:
    - `userID=id`
  - Выводит данные юзера с `userID` = `id`

Пример URL:

`http://hostname/RPi-API/user?userID=1`

Пример вывода:
```json
[
  {
    "id": 1,
    "name": "Tasha",
    "surname": "Bean",
    "email": "quisque.porttitor@outlook.com",
    "phone": "7(904)784-13-11",
    "country": "Russian Federation",
    "city": "Orenburg",
    "money": 1000,
    "itemsInCart": [
      1,
      2,
      3,
      4,
      5
    ],
    "itemsOwns": [
      666,
      444,
      333
    ]
  }
]
```

- **POST**
  - JSON строка (см. описание выше):
    - `{"name": "userName", "surname": "userSurname", "userEmail": "userEmail", "phone": "userPhone", "country": "userCountry", "city": "userCity", "money": userMoney}`
  - Параметры URL:
    - `name=userName`
    - `surname=userSurname`
    - `email=userEmail`
    - `phone=userPhone`
    - `country=userCountry`
    - `city=userCity`
    - `money=userMoney`
  - Вносит юзера в БД с указанными полями и пустыми корзиной и списком обладаемых вещей

Пример URL:

`http://hostname/RPi-API/user?name=someName&surname=someSurname&email=email@email.com&phone=123123&country=Some Country&city=someCity&money=123456`
  
Пробелы в значениях параметров допускаются.

- **PUT**
  - Параметры URL:
    - `name=userName`
    - `surname=userSurname`
    - `email=userEmail`
    - `phone=userPhone`
    - `country=userCountry`
    - `city=userCity`
    - `money=userMoney`
  - Вносит изменения в строку с юзером по указанным параметрам

Пример URL:

`http://hostname/RPi-API/user?name=someName&surname=someSurname&email=email@email.com&phone=123123&country=Some Country&city=someCity&money=123456`

- **DELETE**
  - Параметры URL:
    - `userID=id`
  - Удаляет юзера с `id` из БД. Навсегда и безвозвратно.
***Использовать с оторожностью!***

# ПОКА ЧТО ЭТО ВСЁ