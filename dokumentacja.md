# Opis projektu
Projekt pozwalający firmie na przeprowadzenie oceny ryzyka wypłacalności swojego klienta oraz zaproponowania 
odpowiedniej formy umowy (regular lub pre-paid)

* Pracownik inicjuje nowy proces weryfikacji podając dane dokumentu tożsmaości weryfikowanej osoby
* klient podaje swój dokument tożsamości aby wyświetlić jego proces weryfikacji
* w pierwszym kroku klient uzupełnia dane osobowe oraz wysyła zdjęcie swojego dowodu osobistego
* w drugim kroku klient opcjonalnie wyraża zgodę na pobranie informacji z jego konta bankowego za pomocą open banking (integracja z kontomatik)
* w trzecim kroku model AI analizuje dane osobiste oraz bankowe a następnie ocenia klienta w skali 1-1000, proponuje model umowy oraz uzasadnia swoją decyzję

# Architektura
aplikacja przeglądarkowa + REST api

## frontend
* react + material ui do stylowania aplikacji

## backend 
* java 21 + spring boot framework
* postgresSql
* integracja z OpenAi, użycie modelu gpt-5.4-nano
