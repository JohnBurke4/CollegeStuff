import React, { Component } from "react";
import {Container} from "react-bootstrap"
import Introduction from "./Introduction"
import WelcomeForm from "./WelcomeForm"

class MainBody extends Component {
    constructor(props){
        super(props);
        this.state = {
            hasResult: false
        }
    }
    render() {
        return (
            <Container>
                <Introduction/>
                <WelcomeForm/>
            </Container>
        );
    }

}

export default MainBody;