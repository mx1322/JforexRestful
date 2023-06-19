curl -X POST "http://localhost:8080/api/UpdateConf" -d "userName=DEMO2DcUMH&password=DcUMH"

curl "http://localhost:8080/api/sma?instrument=EURUSD&period=ONE_HOUR&timePeriod=14"

curl "http://localhost:8080/api/sma?instrument=EURUSD&period=ONE_HOUR&timePeriod=14&price=OPEN&side=BID&shift=1"

http://localhost:8080/swagger-ui.html



curl -X GET "http://localhost:8080/api/indicators/bollinger?appliedPrice=CLOSE&BBtimePeriod=20&nbDevUp=2&nbDevDn=2&maType=SMA&shift=1"
