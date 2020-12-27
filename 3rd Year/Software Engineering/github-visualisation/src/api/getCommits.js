async function getCommits(repoOwner, repoName, authToken){
    const header = new Headers({
        'Authorization': `token ${authToken}` 
     });

    let url = `https://api.github.com/repos/${repoOwner}/${repoName}/commits`;
    
    let maxPages = 1;
    let currentPage = 0;
    let result = {}
    while (currentPage < maxPages){
        var data = await fetch(url, 
            {
                method: 'get',
                headers: header
            });
        currentPage++;
    let json = await data.json();
    
    console.log(json);
    }
    return result;
}

export default getCommits;