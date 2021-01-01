async function getCommits(repoOwner, repoName, authToken){
    const header = new Headers({
        'Authorization': `token ${authToken}` 
     });
    
    let maxPages = 10;
    let currentPage = 1;
    let hoursData = new Array(24).fill(0);
    let authorData = {}
    let length = 1;
    let error = "";
    while (currentPage <= maxPages && length !== 0){
        let url = `https://api.github.com/repos/${repoOwner}/${repoName}/commits?per_page=100&page=${currentPage}`
        var data = await fetch(url, 
            {
                method: 'get',
                headers: header
            });
        let json = await data.json();
        length = json.length;
        for(let i = 0; i < length; i++){
            let date = new Date(json[i]["commit"]["committer"]["date"]);
            let author = json[i]["commit"]["author"]["name"];
            if (author in authorData){
                authorData[author]++;
            }
            else{
                authorData[author] = 1;
            }
            let hours = date.getHours();
            hoursData[hours]++;
        }
        currentPage++;
        let status = await data.status;
        if (status !== 200){
            error = json["message"];
            break;
        }
    }
    // for( const [key, value] of Object.entries(authorData)){
    //     console.log(key, value);
    // }
    return {
        hours: hoursData,
        authors: authorData,
        error: error
    };
}

export default getCommits;