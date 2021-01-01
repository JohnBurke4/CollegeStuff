import React, { Component } from "react";
import {Container, Spinner} from "react-bootstrap"
import BarChart from "./BarChart";
import LolipopChart from "./LolipopChart"
import Introduction from "./Introduction"
import WelcomeForm from "./WelcomeForm"
import getCommits from "./../api/getCommits"

class MainBody extends Component {
    constructor(props){
        super(props);
        this.state = {
            authCode: "",
            repoOwner: "",
            repoName: "",
            hasResult: false,
            barchartData: [],
            authorData: [],
            error: "",
            state: 1
        }
        
    }

    updateRepoData = (auth, owner, name) => {
        console.log(auth + " " + owner + " " + name);
        this.setState({
            authCode: auth,
            repoOwner: owner,
            repoName: name,
            state: 2
        });
        getCommits(owner, name, auth).then((data) => {
            console.log(data.error);
            if (data.error === ""){
                this.setState({
                    barchartData: data.hours,
                    authorData: data.authors,
                    state: 3
                });
            }
            else {
                this.setState({
                    error: `Error: ${data.error} please try again.`,
                    state: 1
                });
            }
            
        });

    }
    render() {
        let body;
        if (this.state.state === 1){
            body = <div>
                <WelcomeForm dataFunction={this.updateRepoData}/>
                <h3 className="mt-5">{this.state.error}</h3>
                </div>
        }
        else if (this.state.state === 2){
            body =  <div className="d-flex justify-content-center">
                        <Spinner animation="border" variant="primary" />
                    </div>
        }
        else if (this.state.state === 3){

            body = <Container>
                <a href={`https://github.com/${this.state.repoOwner}/${this.state.repoName}`}><h3>{`https://github.com/${this.state.repoOwner}/${this.state.repoName}`}</h3></a>
                <h4>Commits analysed: {this.state.barchartData.reduce((a,b) => a+b)}</h4>
                <BarChart data={this.state.barchartData} margin="50" height="600" width="1000"/>
                <LolipopChart data={this.state.authorData} margin="25" height="600" width="1000" />
            </Container>
            
            
        }
        return (
            <Container>
                <Introduction/>
                <Container className="justify-content-center mb-5">
                    {body}
                    
                </Container>
            </Container>
        );
    }

}

export default MainBody;