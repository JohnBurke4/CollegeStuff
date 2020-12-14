import requests

repoName = input("Please input a repo name: ")
f = open("pass.txt", "r")
authCode = str(f.read())
print(authCode)
headers = {"Authorization": "token %s" % authCode}
requestURL = "https://api.github.com/repos/torvalds/linux/commits"
r = requests.get(requestURL, headers=headers)
print(r.text)
