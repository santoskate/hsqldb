1. Execute: run_server_hsqlfb.bat (Server)
2. Execute: run_DBManager_hsqlfb.bat (SQBD)
3. On SGBD connection screen:
	Recent -> HSQL Database Engine
	Type -> HSQL Database Engine WebServer
	URL -> jdbc:hsqldb:http://localhost:88/moss_test
4. OK

Ready to test database with new I18n module!


-----
Queries and comands examples:

select top 1 
	CODE, 
	TEXT, 
	bundle('0002', '555555', 'it') text2, 
	bundle('0002', '555555', 'fr_FR') text3 
from msg_result;

CALL bundle('0002', '555555', 'it');