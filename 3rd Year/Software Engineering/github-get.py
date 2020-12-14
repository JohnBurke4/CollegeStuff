import requests

repoOwner = input("Please input the repo owner: ")
repoName = input("Please input the repo name: ")

f = open("pass.txt", "r")
authCode = str(f.read())

headers = {"Authorization": "token %s" % authCode}
requestURL = "https://api.github.com/repos/%s/%s/commits" % repoOwner, repoName

r = requests.get(requestURL, headers=headers)

print(r.text)
