import requests
import json
from datetime import datetime

repoOwner = input("Please input the repo owner: ")
repoName = input("Please input the repo name: ")

f = open("pass.txt", "r")
authCode = str(f.read())

headers = {"Authorization": "token %s" % authCode}
requestURL = "https://api.github.com/repos/%s/%s/commits" % (
    repoOwner, repoName)

r = requests.get(requestURL, headers=headers)

resultJSON = json.loads(r.text)
print(r.links["next"]["url"])
timeDict = {}
for i in resultJSON:
    date = datetime.strptime(
        i["commit"]["committer"]["date"], "%Y-%m-%dT%H:%M:%SZ")
    if date.hour in timeDict:
        timeDict[date.hour] += 1
    else:
        timeDict[date.hour] = 1

print(timeDict)
