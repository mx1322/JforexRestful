curl -X POST "http://localhost:8080/api/config/update" -d "userName=DEMO2wJcsq&password=wJcsq"

curl "http://localhost:8080/api/sma?instrument=EURUSD&period=ONE_HOUR&timePeriod=14"

curl "http://localhost:8080/api/sma?instrument=EURUSD&period=ONE_HOUR&timePeriod=14&price=OPEN&side=BID&shift=1"

http://localhost:8080/swagger-ui.html