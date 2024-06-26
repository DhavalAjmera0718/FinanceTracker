1]SAVE DATA API = http://localhost:7777/wallet/saveWalletData {
  
    "name": "DHAVAL AJMERA",
    "accountType": "Savings",
    "balance": 100000.50
}
2 ]*********************************************************************************************************
3] GET WALLET BY ID = http://localhost:7777/wallet/getWalletDataByid/1
4] DELETE WALLET DATA BY ID =  http://localhost:7777/wallet/deleteDataById/1
5] UPDATE WALLET DATA BY ID = http://localhost:7777/wallet/updateWalletData/2
[{
  
    "name": "DHAVAL AJMERA",
    "accountType": "Savings",
    "balance": 1000.50
}]
6] ******************************************************************************************************************
DEPOSITE MONEY BY CASH - http://localhost:7777/wallet/depositeMoney/{AC NUMBER IN DB }
{
    "bankDeposite":50000
}
7] ******************************************************************************************************************
WITHDRAW MONEY BY CASH - http://localhost:7777/wallet/withDrawMoney/{ACCOUNT NUMBER IN DB }
{
    "bankWithDraw":10000
}

7] ********************************************************************************************************************
AMOUNT TRANSFER MY BANK TO DIFFRENT BANK - http://localhost:7777/wallet/myWalletToDiffWallet/{ACCOUNT NUMBER IN DB}

{
    "receiverBankAc": "SBI456565655",
    "sendMoney": 100000.00
}
8 ]************************************************************************************************************************
DEPOSITE COMES DROM DIFF BANK ACCOUNT - http://localhost:7777/wallet/DepositesFromAnotherAccount/{ACCOUNT NUMBER FROM DB }
{
    "senderBankAc":"SBI7878989854",
    "receiveMoney":1000
}
