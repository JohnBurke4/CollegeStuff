import React, { Component } from "react";
import {Container, Row, Col} from "react-bootstrap"
import Introduction from "./Introduction"
import WelcomeForm from "./WelcomeForm"

class MainBody extends Component {
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