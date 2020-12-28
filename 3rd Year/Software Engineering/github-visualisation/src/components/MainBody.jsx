import React, { Component } from "react";
import {Container} from "react-bootstrap"
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
            repoName: name
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
            //body = <DataLoading/>
        }
        else if (this.state.state === 3){
            console.log("changing");
            console.log(this.state.barchartData);
            
        }
        return (
            <Container>
                <Introduction/>
                <Container className="justify-content-center">
                    {body}
                    <BarChart data={this.state.barchartData} margin="60" height="600" width="1200"/>
                </Container>
            </Container>
        );
    }

}

export default MainBody;