async function getCommits(repoOwner, repoName, authToken){
    const header = new Headers({
        'Authorization': `token ${authToken}` 
     });
    
    let maxPages = 10;
    let currentPage = 1;
    let result = new Array(24).fill(0);
    while (currentPage < maxPages){
        let url = `https://api.github.com/repos/${repoOwner}/${repoName}/commits?per_page=100&page=${currentPage}`
        var data = await fetch(url, 
            {
                method: 'get',
                headers: header
            });
        let json = await data.json();
        let length = json.length;
        for(let i = 0; i < length; i++){
            let date = new Date(json[i]["commit"]["committer"]["date"]);
            let hours = date.getHours();
            result[hours]++;
        }

        
        currentPage++;
    }
    for(let i = 0; i < 24; i++){
        console.log("Hour " + i + ": " +  result[i] + " commits");
    }
    return result;
}

export default getCommits;