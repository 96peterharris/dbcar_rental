<h1>CAR RENTAL BACKEND PROJECT</h1>
This is a project of car rental backand in which we use REST API and SQLite database.<br>
In database we have two columns "car" with fields("car_id","brand","model","user_id")
and "client" with fields("user_id","first_name","last_name").
<h2>Functionalities</h2>
Functionalities:
<ul>
    <li>getAllCar() - return list of all cars from table "car" ("/car-rental/cars")</li>
    <li>getCarById(@PathVariable Long car_id) - return car with specified "car_id" ("/car-rental/cars/{car_id}")</li>
    <li>addCar(@RequestBody Car newCar) - add new car to table "car" ("/car-rental/cars")</li>
    <li>editCar(@RequestBody Car editedCar, @PathVariable Long car_id) - edit car with specified "car_id" ("/car-rental/cars/edit/{car_id}")</li>
    <li>giveBackCar(@PathVariable Long car_id) - this functionality allows to give back rented car with specified "car_id" ("/car-rental/cars/giveBack/{car_id}")</li>
    <li>rentCar(@RequestParam Long car_id, @RequestParam Long client_id) - this functionality allows to rent a car if it has not been rented with
    specified "car_id" and "client_id" ("/cars-rental/cars/rent/{?car_id=value&client_id=value}")</li>
    <li>deleteCar(@PathVariable Long car_id) - this functionality allows to delete car with specified "car_id" from table "car" ("/car-rental/cars/{car_id}") </li>
    <li>getAllClients() - return list of all clients from table "client" ("/car-rental/clients")</li>
    <li>getClientById(@PathVariable Long client_id) - return client with specified "client_id" ("/car-rental/clients")</li>
    <li>editClient(@RequestBody Client editedClient, @PathVariable Long client_id) - edit car with specified "client_id" ("/car-rental/clients/edit/{client_id}")</li>
    <li>deleteClient(@PathVariable Long client_id) - this functionality allows to delete client with specified "client_id" from table "client" ("/car-rental/clients/{client_id}")</li>
</ul>
<h2>How to run</h2>
<li>The complete version we can download form Release section on github where is .zip which we 
    should unpack. Then after that we should run "db-car_rental.jar" using command "java -jar db-car_rental.jar".</li>
<li>Another option is that we can clone project from gtiHub repository and run as Maven project inside IDE where 
    we can run all project or only tests.</li>
<li>Link to postman collection created to test this API: https://www.getpostman.com/collections/7ac3f77a4a4b8fb927dc</li>





