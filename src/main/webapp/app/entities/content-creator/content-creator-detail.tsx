import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-creator.reducer';

export const ContentCreatorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const contentCreatorEntity = useAppSelector(state => state.contentCreator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentCreatorDetailsHeading">
          <Translate contentKey="clapApplicationApp.contentCreator.detail.title">ContentCreator</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contentCreatorEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="clapApplicationApp.contentCreator.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{contentCreatorEntity.fullName}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="clapApplicationApp.contentCreator.country">Country</Translate>
            </span>
          </dt>
          <dd>{contentCreatorEntity.country}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="clapApplicationApp.contentCreator.city">City</Translate>
            </span>
          </dt>
          <dd>{contentCreatorEntity.city}</dd>
        </dl>
        <Button tag={Link} to="/content-creator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-creator/${contentCreatorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentCreatorDetail;
