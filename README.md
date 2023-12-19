<p>Запросы для создания базы данных и таблицы находится в файле resources/sql/queries.sql.</p>
<p>При отправке строки "{\"id\":null,\"firstname\":\"Alex\",\"lastname\":\"Murfhy\",
\"dateOfBirth\":\"1997-05-17\",\"email\":\"1234@gmail.com\"}" получаем готовый объект типа UserDto.</p>
<p>Варианты запросов есть в файле Main.java в папке runner.</p>
<p>При создании файла pdf в директории resources/pdf/ создается новая директория с текущей датой
(при ее отсутствии). Метод для создания pdf вызывается после вызова методов сервиса findUserById,
createUser и updateUser. В структуре названия файла есть id объекта User. Если в директории уже
присутствует информация о User с таким же id, то данный файл перезаписывается.</p>
<p>Использовались следующие паттерны: singleton(UserDaoImpl, UserServiceImpl, 
UserPdfGenerator и UserDtoValidator), builder(User, UserDto) и factory
(для создания кешей)</p>