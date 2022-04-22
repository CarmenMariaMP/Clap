import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './privacy-request.reducer';

export const PrivacyRequestDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const privacyRequestEntity = useAppSelector(state => state.privacyRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="privacyRequestDetailsHeading">
          <Translate contentKey="clapApplicationApp.privacyRequest.detail.title">PrivacyRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{privacyRequestEntity.id}</dd>
          <dt>
            <span id="requestState">
              <Translate contentKey="clapApplicationApp.privacyRequest.requestState">Request State</Translate>
            </span>
          </dt>
          <dd>{privacyRequestEntity.requestState}</dd>
          <dt>
            <span id="requestDate">
              <Translate contentKey="clapApplicationApp.privacyRequest.requestDate">Request Date</Translate>
            </span>
          </dt>
          <dd>
            {privacyRequestEntity.requestDate ? (
              <TextFormat value={privacyRequestEntity.requestDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="clapApplicationApp.privacyRequest.company">Company</Translate>
          </dt>
          <dd>{privacyRequestEntity.company ? privacyRequestEntity.company.id : ''}</dd>
          <dt>
            <Translate contentKey="clapApplicationApp.privacyRequest.contentCreatto">Content Creatto</Translate>
          </dt>
          <dd>{privacyRequestEntity.contentCreatto ? privacyRequestEntity.contentCreatto.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/privacy-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/privacy-request/${privacyRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrivacyRequestDetail;
