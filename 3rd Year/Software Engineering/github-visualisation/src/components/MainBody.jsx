import React, { Component } from "react";
import {Container, Spinner} from "react-bootstrap"
import BarChart from "./BarChart";
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
            console.log(data);
            this.setState({
                barchartData: data,
                state: 3
            });
        });

    }
    render() {
        let body;
        if (this.state.state === 1){
            body = <WelcomeForm dataFunction={this.updateRepoData}/>
        }
        else if (this.state.state === 2){
            body =  <div className="d-flex justify-content-center">
                        <Spinner animation="border" variant="primary" />
                    </div>
        }
        else if (this.state.state === 3){

            body = <Container>
                <a href={`https://github.com/${this.state.repoOwner}/${this.state.repoName}`}><h3>{`https://github.com/${this.state.repoOwner}/${this.state.repoName}`}</h3></a>
                <BarChart data={this.state.barchartData} margin="50" height="600" width="1000"/>
            </Container>
            
            
        }
        return (
            <Container>
                <Introduction/>
                <Container className="justify-content-center">
                    {body}
                    
                </Container>
            </Container>
        );
    }

}

export default MainBody;