import requests
import json
from datetime import datetime

repoOwner = input("Please input the repo owner: ")
repoName = input("Please input the repo name: ")
loops = int(input("How many pages do you want to check: "))

f = open("pass.txt", "r")
authCode = str(f.read())

headers = {"Authorization": "token %s" % authCode}
requestURL = "https://api.github.com/repos/%s/%s/commits" % (
    repoOwner, repoName)

r = requests.get(requestURL, headers=headers)

resultJSON = json.loads(r.text)
timeDict = {}
counter = 0
while (counter != loops and r.status_code == 200):
    print("Page: %s" % str(counter+1))
    for i in resultJSON:
        date = datetime.strptime(
            i["commit"]["committer"]["date"], "%Y-%m-%dT%H:%M:%SZ")
        if date.hour in timeDict:
            timeDict[date.hour] += 1
        else:
            timeDict[date.hour] = 1

    if "next" in r.links:
        requestURL = r.links["next"]["url"]
        r = requests.get(requestURL, headers=headers)
        resultJSON = json.loads(r.text)
        counter += 1
    else:
        break


resultFile = open("result.txt", "w")
resultFile.write("User: %s, Repo: %s\n" % (repoOwner, repoName)
for i in range(0, 24):
    if i in timeDict:
        resultFile.write("Hour: %s Number of commits: %s\n" %
                         (str(i), timeDict[i]))
